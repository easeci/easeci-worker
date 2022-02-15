package io.easeci.worker.connect.state;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConnectionStateResponse extends ConnectionStateRequest {
    private NodeConnectionState nodeConnectionState;
}
