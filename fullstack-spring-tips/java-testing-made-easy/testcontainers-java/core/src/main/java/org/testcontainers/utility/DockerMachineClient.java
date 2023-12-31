package org.testcontainers.utility;

import lombok.NonNull;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by rnorth on 27/10/2015.
 */
public class DockerMachineClient {

    private static DockerMachineClient instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerMachineClient.class);

    private static final String executableName;

    static {
        if (SystemUtils.IS_OS_WINDOWS) {
            executableName = "docker-machine.exe";
        } else {
            executableName = "docker-machine";
        }
    }

    /**
     * Private constructor
     */
    private DockerMachineClient() {}

    /**
     * Obtain an instance of the DockerMachineClient wrapper.
     *
     * @return the singleton instance of DockerMachineClient
     */
    public static synchronized DockerMachineClient instance() {
        if (instance == null) {
            instance = new DockerMachineClient();
        }

        return instance;
    }

    public boolean isInstalled() {
        return CommandLine.executableExists(executableName);
    }

    public Optional<String> getDefaultMachine() {
        String ls = CommandLine.runShellCommand(executableName, "ls", "-q");
        List<String> machineNames = Arrays.asList(ls.split("\n"));

        String envMachineName = System.getenv("DOCKER_MACHINE_NAME");

        if (machineNames.contains(envMachineName)) {
            LOGGER.debug("Using docker-machine set in DOCKER_MACHINE_NAME: {}", envMachineName);
            return Optional.of(envMachineName);
        } else if (machineNames.contains("default")) {
            LOGGER.debug("DOCKER_MACHINE_NAME is not set; Using 'default' docker-machine", envMachineName);
            return Optional.of("default");
        } else if (machineNames.size() > 0) {
            LOGGER.debug(
                "DOCKER_MACHINE_NAME is not set and no machine named 'default' found; Using first machine found with `docker-machine ls`: {}",
                machineNames.get(0)
            );
            return Optional.of(machineNames.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void ensureMachineRunning(@NonNull String machineName) {
        if (!isMachineRunning(machineName)) {
            LOGGER.info("Docker-machine '{}' is not running. Will start it now", machineName);
            CommandLine.runShellCommand("docker-machine", "start", machineName);
        }
    }

    /**
     * @deprecated Use getDockerDaemonUrl(@NonNull String machineName) for connection to docker-machine
     */
    @Deprecated
    public String getDockerDaemonIpAddress(@NonNull String machineName) {
        return CommandLine.runShellCommand(executableName, "ip", machineName);
    }

    public String getDockerDaemonUrl(@NonNull String machineName) {
        return CommandLine.runShellCommand(executableName, "url", machineName);
    }

    public boolean isMachineRunning(String machineName) {
        String status = CommandLine.runShellCommand("docker-machine", "status", machineName);
        return status.trim().equalsIgnoreCase("running");
    }

    public boolean isDefaultMachineRunning() {
        return isMachineRunning(getDefaultMachine().orElse("default"));
    }
}
