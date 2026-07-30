package schwarz.jobs.interview.coupon.core.persistence.mapper;

import java.util.List;

/**
 * Converts between domain objects and their JPA entities.
 */
public interface DatabaseMapper<DOMAIN, ENTITY> {

    /**
     * Turns a domain object into an entity ready to persist.
     */
    ENTITY domainToEntity(DOMAIN domain);

    /**
     * Turns a persisted entity into a domain object.
     */
    DOMAIN entityToDomain(ENTITY entity);

    /**
     * Same as {@link #entityToDomain(Object)}, for a batch of entities.
     */
    List<DOMAIN> entityToDomain(Iterable<ENTITY> entityList);

}
