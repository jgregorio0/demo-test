package dev.jgregorio.demo.data.application.in.center;

import dev.jgregorio.demo.data.application.in.common.usecase.*;
import dev.jgregorio.demo.data.domain.center.*;

public interface CenterUseCase
        extends CreateUseCase<Center, CenterCreation>,
        ReadUseCase<Center, CenterRead>,
        UpdateUseCase<Center, CenterUpdate>,
        DeleteUseCase<CenterDelete>,
        SearchUseCase<Center, CenterSearch> {
}
