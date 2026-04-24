package dev.jgregorio.demo.data.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static String formatMessage(Class<?> clazz, Object id) {
        return String.format(
                "Resource [%s] with id [%s] not found", clazz.getSimpleName(), id.toString());
    }
}
