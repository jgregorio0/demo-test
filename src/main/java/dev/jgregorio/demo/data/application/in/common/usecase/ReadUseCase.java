package dev.jgregorio.demo.data.application.in.common.usecase;

public interface ReadUseCase<D, R> {

    D read(R toRead);
}
