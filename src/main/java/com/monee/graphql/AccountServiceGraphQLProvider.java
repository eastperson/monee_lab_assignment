package com.monee.graphql;

import com.monee.dao.AccountDao;
import com.monee.graphql.DataFetcher.AccountDataFetcher;
import com.monee.graphql.DataFetcher.AllAccountDataFetcher;
import com.monee.model.Account;
import com.monee.model.Post;
import com.monee.service.AccountService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.stream.Stream;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;


public class AccountServiceGraphQLProvider implements AccountServiceGraphQL {

    private final AccountDao accountDao;
    private final AllAccountDataFetcher allAccountDataFetcher;
    private final AccountDataFetcher accountDataFetcher;
    private GraphQL graphQL;


    public AccountServiceGraphQLProvider(AccountDao accountDao, AccountDataFetcher accountDataFetcher, AllAccountDataFetcher allAccountDataFetcher){
        this.accountDao = accountDao;
        this.accountDataFetcher = accountDataFetcher;
        this.allAccountDataFetcher = allAccountDataFetcher;
    }

    public ExecutionResult execute(String query){
        return graphQL.execute(query);
    }

    public void loadSchema() throws IOException {

        try{

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
                            .dataFetcher("Account",accountDataFetcher))
                    .build();
            SchemaGenerator schemaGenerator = new SchemaGenerator();
            GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

            GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

            this.graphQL = build;
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("스키마 오류");
        }
    }

}
