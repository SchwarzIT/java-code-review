package schwarz.jobs.interview.coupon.web.mapper;

/**
 * Converts an incoming request DTO into a domain object.
 */
public interface RequestMapper<I, R> {

    R toDomain(I input);

}
