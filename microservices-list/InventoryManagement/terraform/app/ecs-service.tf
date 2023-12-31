resource "aws_ecs_task_definition" "task_definition" {
  family = var.app_name

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  task_role_arn            = aws_iam_role.task_definition_task_role.arn

  execution_role_arn = aws_iam_role.task_definition_execution_role.arn

  cpu    = var.cpu
  memory = var.memory

  container_definitions = jsonencode([
    {
      name  = var.app_name
      image = aws_ecr_repository.app_repository.repository_url

      cpu    = var.cpu
      memory = var.memory

      environment = [
        {
          name = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name = "SERVER_PORT",
          value = "80"
        },
        {
          name  = "AWS_S3_BUCKET"
          value = aws_s3_bucket.app_storage_bucket.bucket
        },
        {
          name  = "SPRINGDOC_SWAGGER_UI_PATH"
          value = "/docs"
        }
      ]

      secrets = [
        {
          name = "SPRING_DATA_MONGODB_URI"
          valueFrom = aws_secretsmanager_secret.mongodb_connection_string.arn
        }
      ]

      essential    = true
      portMappings = [
        {
          containerPort = 80
          protocol = "tcp"
          hostPort      = 80
        }
      ]

      logConfiguration : {
        "logDriver" : "awslogs",
        "options": {
          "awslogs-group" : aws_cloudwatch_log_group.log_group.name,
          "awslogs-region" : var.region,
          "awslogs-stream-prefix" : var.app_name
        }
      },
    }
  ])
}

resource "aws_ecs_service" "inventory_management_service" {
  name            = var.app_name
  cluster         = module.ecs_cluster.cluster_id
  task_definition = aws_ecs_task_definition.task_definition.arn
  depends_on      = [aws_s3_bucket.app_storage_bucket]

  network_configuration {
    security_groups = [aws_security_group.alb-access.id]
    subnets          = module.vpc.public_subnets
    assign_public_ip = true
  }

  // Ignoring these because CD tool will change those to kick off a new deployment
  lifecycle {
    ignore_changes = [desired_count, task_definition]
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.target_group.arn
    container_name = aws_ecs_task_definition.task_definition.family
    container_port = 80
  }
}
