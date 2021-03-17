package com.monee.graphql;

import graphql.ExecutionResult;

public interface AccountServiceGraphQL {

    ExecutionResult execute(String query);

}
