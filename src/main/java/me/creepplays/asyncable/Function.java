package me.creepplays.asyncable;

import java.sql.SQLException;

@FunctionalInterface
public interface Function<P, R> {

    R run(P value) throws Exception;

}
