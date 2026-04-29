package schwarz.jobs.interview.coupon.common.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import schwarz.jobs.interview.coupon.common.service.MessageService;
import schwarz.jobs.interview.coupon.common.util.MessageKey;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class AppResponseDTOMapper {

    @Autowired
    protected MessageService messageService;

    @Mapping(target = "message", expression = "java(messageService.message(messageKey.key()))")
    @Mapping(target = "code", expression = "java(messageKey.name())")
    @Mapping(target = "errors", ignore = true)
    public abstract AppResponseDto<Object> mapSuccess(final MessageKey messageKey, final Object data);

    @Mapping(target = "message", expression = "java(messageService.message(messageKey.key()))")
    @Mapping(target = "code", expression = "java(messageKey.name())")
    @Mapping(target = "data", ignore = true)
    public abstract AppResponseDto<Void> mapError(final MessageKey messageKey, final List<AppResponseDto.Error> errors);

    @Mapping(target = "message", expression = "java(messageService.message(messageKey.key(),args))")
    @Mapping(target = "code", expression = "java(messageKey.name())")
    @Mapping(target = "data", ignore = true)
    @Mapping(target = "errors", ignore = true)
    public abstract AppResponseDto<Void> mapResponse(final MessageKey messageKey, final Object... args);

    public abstract AppResponseDto.Error mapViolation(final String field, final String message);
}
