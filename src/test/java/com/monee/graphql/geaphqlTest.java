package com.monee.graphql;


import com.monee.dao.AccountDao;
import com.monee.graphql.DataFetcher.account.AccountDataFetcher;
import com.monee.graphql.DataFetcher.account.AllAccountDataFetcher;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import graphql.schema.idl.SchemaParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class geaphqlTest {

    private static Logger log = LoggerFactory.getLogger(geaphqlTest.class);

    @DisplayName("schema 테스트")
    @Test
    void schema() throws IOException {
        String schema = "type Query{hello:String}";

        SchemaParser schemaParser = new SchemaParser();

        log.info("schema : " + schema);

        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        log.info("typeDefinitionRegistry : " + typeDefinitionRegistry.getType("Query"));

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello",new StaticDataFetcher("world")))
                .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

        log.info("graphQL schema : "+graphQLSchema.getAllTypesAsList());

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        log.info("build : " + build);

        ExecutionResult executionResult = build.execute("{hello}");

        log.info(executionResult.getData().toString());
    }

    @DisplayName("schma file테스트")
    @Test
    void schema_file() throws IOException {

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);

        File schemaFile = new File("C:\\Users\\kjuio\\IdeaProjects\\monee_lab_assignment\\src\\main\\resources\\graphql\\" + "schema.graphqls");

        log.info("schema : " + schemaFile);

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);

        log.info("typeDefinitionRegistry account : " + typeDefinitionRegistry.getType("Account"));
        log.info("typeDefinitionRegistry post : " + typeDefinitionRegistry.getType("Post"));
        log.info("typeDefinitionRegistry reply : " + typeDefinitionRegistry.getType("Reply"));
        log.info("typeDefinitionRegistry role : " + typeDefinitionRegistry.getType("Role"));
        log.info("typeDefinitionRegistry query : " + typeDefinitionRegistry.getType("Query"));

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder
                    .dataFetcher("allAccounts",allAccountDataFetcher)
                    .dataFetcher("account",accountDataFetcher))
                    .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionResult executionResult = build.execute("{allAccounts{success,status,data{seq,email,nickname,seq}}}");

        log.info(executionResult.getData().toString());
    }

//    private static GraphQLSchema buildSchema(){
//        AccountDao accountDao = new AccountDao();
//        return SchemaParser
//                .newParser()
//                .file("schema.graphqls")
//                .resolvers(new Query(accountDao))
//                .build().makeExecutableSchema();
//    }

}
