package dev.jgregorio.demo.data.infrastructure.api;

public interface GenericRequestApiMapper<R, D> {

    D fromRequest(R request);
}
