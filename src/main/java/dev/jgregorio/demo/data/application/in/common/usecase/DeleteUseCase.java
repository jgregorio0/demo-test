package dev.jgregorio.demo.data.application.in.common.usecase;

public interface DeleteUseCase<D> {

    void delete(D toDelete);
}
