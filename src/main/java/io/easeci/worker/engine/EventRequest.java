package io.easeci.worker.engine;

import java.util.List;
import java.util.UUID;

record EventRequest(UUID pipelineContextId,
                    UUID workerNodeId,
                    String workerNodeHostname,
                    List<IncomingLogEntry> incomingLogEntries) {
}
