package com.dansiwiec.payments

import com.dansiwiec.payments.models.*
import com.dansiwiec.payments.repos.CustomerRepo
import com.dansiwiec.payments.repos.SkuRepo
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.test.utils.KafkaTestUtils

class ServiceTest(@Autowired val skuRepo: SkuRepo, @Autowired var customerRepo: CustomerRepo) : TestBase() {

        @BeforeEach
        fun init() {
            kafkaClient = createKafkaClient()
            wireMockServer.resetAll()
        }

        @Test
        fun testSuccessfulOrder() {
            // GIVEN
            kafkaClient.send(Topics.CUSTOMERS, Customer(id = "1", state = State.CA, accountNumber = 123))
            kafkaClient.send(Topics.SKUS, Sku("1", "Lawnmower", 750.0))
            wireMockServer.stubFor(post("/api/payment").willReturn(aResponse().withStatus(HttpStatus.SC_OK)))
            await().until(this::storesPopulated)

            // WHEN
            kafkaClient.send(Topics.ORDERS, Order("1", listOf(LineItem("1", 2)), "1"))

            // THEN
            val paymentMessage = kafkaClient.receive(Topics.PAYMENTS)
            assertThat(paymentMessage.value().id).isEqualTo("1")
            assertThat(paymentMessage.value().status).isEqualTo(Payment.Status.PAID)

            wireMockServer.verify(
                1,
                postRequestedFor(urlEqualTo("/api/payment")).withRequestBody(
                    equalToJson("""{"accountNumber": 123, "total": 1608.75}""")
                )
            )
        }

        @Test
        fun testFailedPayment() {
            wireMockServer.stubFor(post("/api/payment").willReturn(aResponse().withStatus(HttpStatus.SC_CONFLICT)))
            kafkaClient.send(Topics.CUSTOMERS, Customer(id = "1", state = State.CA, accountNumber = 123))
            kafkaClient.send(Topics.SKUS, Sku("1", "Lawnmower", 750.0))
            await().until(this::storesPopulated)

            // WHEN
            kafkaClient.send(Topics.ORDERS, Order("1", listOf(LineItem("1", 2)), "1"))

            // THEN
            val paymentMessage = kafkaClient.receive(Topics.PAYMENTS)
            assertThat(paymentMessage.value().id).isEqualTo("1")
            assertThat(paymentMessage.value().status).isEqualTo(Payment.Status.FAILED)
        }

        private fun storesPopulated(): Boolean = skuRepo.skus.isNotEmpty() && customerRepo.customers.isNotEmpty()

    }
