package schwarz.jobs.interview.coupon.web.mapper;

/**
 * Converts an incoming request DTO into a command object for the service layer.
 */
public interface CommandMapper<I, R> {

    R toCommand(I input);

}
