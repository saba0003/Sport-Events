package com.example.demo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public class ExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    public static <T, ID extends Number> void invalidIdHandler(ID id, JpaRepository<T, ID> repository) {
        if (id == null) {
            logger.error("Invalid ID!");
            throw new IllegalArgumentException("Invalid ID!");
        }
        if (repository.findById(id).isEmpty()) {
            logger.info(String.format("Player with ID %d doesn't exist!", id.longValue()));
            throw new IllegalArgumentException("Wrong ID!");
        }
    }

    public static <T extends Number> void invalidNumberHandler(T number) {
        if (number == null) {
            logger.error("Invalid number!");
            throw new IllegalArgumentException("Invalid number!");
        }

        if (number.longValue() == 0) {
            logger.error("Player number can't be zero!");
            throw new IllegalArgumentException("Invalid number!");
        }
        if (number.longValue() < 0) {
            logger.error("Player number can't be negative!");
            throw new IllegalArgumentException("Invalid number!");
        }
    }

    public static void invalidObjectHandler(Object o) {
        if (o == null) {
            logger.error("Invalid " + Object.class.getSimpleName());
            throw new IllegalArgumentException("Invalid object!");
        }
    }
}
