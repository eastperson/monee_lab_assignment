package com.monee.graphql;

import graphql.ExecutionResult;

public interface ServiceGraphQL {

    ExecutionResult execute(String query);

}
