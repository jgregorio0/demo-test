package dev.jgregorio.demo.data.application.in.common.usecase;

public interface UpdateUseCase<D, U> {

    D update(U toUpdate);
}
