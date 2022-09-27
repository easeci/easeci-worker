package io.easeci.worker.connect.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingStateResponse {
    private NodeProcessingState nodeProcessingState;
    private Date nodeProcessingStateCheckDate;
}
