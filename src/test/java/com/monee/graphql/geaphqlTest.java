package com.monee.graphql;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.monee.dao.AccountDao;
import com.monee.graphql.DataFetcher.AccountDataFetcher;
import com.monee.graphql.DataFetcher.AllAccountDataFetcher;
import com.monee.graphql.resolver.AccountResolver;
import com.monee.model.Account;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
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
import java.util.List;

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

        ExecutionResult executionResult = build.execute("{allAccounts{email,nickname}}");

        System.out.println(executionResult.getData().toString());
    }

    @DisplayName("AccountServiceGraphQLProvide accounts 테스트")
    @Test
    void graphqlProvider_Test() throws IOException {

        String query = "{allAccounts{email,nickname,seq}}";

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao,accountDataFetcher,allAccountDataFetcher);
        accountServiceGraphQLProvider.loadSchema();

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        System.out.println(result.getData().toString());

    }

    @DisplayName("AccountServiceGraphQLProvide account 테스트")
    @Test
   void graphqlProvider_account_test() throws IOException {

        AccountDao accountDao = new AccountDao();
        AccountDataFetcher accountDataFetcher = new AccountDataFetcher(accountDao);
        AllAccountDataFetcher allAccountDataFetcher = new AllAccountDataFetcher(accountDao);
        AccountServiceGraphQLProvider accountServiceGraphQLProvider = new AccountServiceGraphQLProvider(accountDao,accountDataFetcher,allAccountDataFetcher);
        accountServiceGraphQLProvider.loadSchema();

        String query = "{Account(seq:1){seq,nickname}}";
        //query = "{allAccounts{email,nickname,seq}}";

        System.out.println(query);

        ExecutionResult result = accountServiceGraphQLProvider.execute(query);
        System.out.println(result.getData().toString());

    }

    @DisplayName("Query getAccounts 클래스")
    @Test
    void query_accounts(){
        AccountDao accountDao = new AccountDao();
        Query query = new Query(accountDao);
        List<Account> accounts = query.getAccounts();

        assertNotNull(accounts);
        System.out.println(accounts);
    }

    @DisplayName("Query getAccount 클래스")
    @Test
    void query_account(){
        AccountDao accountDao = new AccountDao();
        Query query = new Query(accountDao);
        Account account = query.getAccount(1L);

        assertNotNull(account);
        System.out.println(account);
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
