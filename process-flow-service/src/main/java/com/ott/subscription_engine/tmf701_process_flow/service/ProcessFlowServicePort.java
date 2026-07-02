package com.ott.subscription_engine.tmf701_process_flow.service;

import com.ott.subscription_engine.tmf701_process_flow.model.ProcessFlow;
import com.ott.subscription_engine.tmf701_process_flow.model.ProcessFlowCreate;
import java.util.Optional;

/**
 * Public API of the TMF701 Process Flow module (orchestration entry point).
 */
public interface ProcessFlowServicePort {
    ProcessFlow createAndExecuteProcessFlow(ProcessFlowCreate dto);
    Optional<ProcessFlow> getProcessFlowById(String id);
}
