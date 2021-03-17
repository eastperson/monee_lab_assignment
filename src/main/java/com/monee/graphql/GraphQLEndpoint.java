package com.monee.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;

public class GraphQLEndpoint{

    public GraphQLEndpoint(GraphQLSchema schema){
        SchemaParser.newParser().file("graphql/schema.graphqls").build().makeExecutableSchema();
    }
}
