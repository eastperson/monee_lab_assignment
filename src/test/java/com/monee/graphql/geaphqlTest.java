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

import java.io.File;
import java.io.IOException;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class geaphqlTest {


    @DisplayName("schema 테스트")
    @Test
    void schema() throws IOException {
        String schema = "type Query{hello:String}";

        SchemaParser schemaParser = new SchemaParser();

        System.out.println("schema : " + schema);

        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        System.out.println("typeDefinitionRegistry : " + typeDefinitionRegistry.getType("Query"));

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello",new StaticDataFetcher("world")))
                .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

        System.out.println("graphQL schema : "+graphQLSchema.getAllTypesAsList());

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        System.out.println("build : " + build);

        ExecutionResult executionResult = build.execute("{hello}");

        System.out.println(executionResult.getData().toString());
    }

    @DisplayName("schma file테스트")
    @Test
    void schema_file() throws IOException {

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);

        File schemaFile = new File("C:\\Users\\kjuio\\IdeaProjects\\monee_lab_assignment\\src\\main\\resources\\graphql\\" + "schema.graphqls");

        System.out.println("schema : " + schemaFile);

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);

        System.out.println("typeDefinitionRegistry account : " + typeDefinitionRegistry.getType("Account"));
        System.out.println("typeDefinitionRegistry post : " + typeDefinitionRegistry.getType("Post"));
        System.out.println("typeDefinitionRegistry reply : " + typeDefinitionRegistry.getType("Reply"));
        System.out.println("typeDefinitionRegistry role : " + typeDefinitionRegistry.getType("Role"));
        System.out.println("typeDefinitionRegistry query : " + typeDefinitionRegistry.getType("Query"));

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder
                    .dataFetcher("allAccounts",allAccountDataFetcher)
                    .dataFetcher("account",accountDataFetcher))
                    .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionResult executionResult = build.execute("{allAccounts{success,status,data{seq,email,nickname,seq}}}");

        System.out.println(executionResult.getData().toString());
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
