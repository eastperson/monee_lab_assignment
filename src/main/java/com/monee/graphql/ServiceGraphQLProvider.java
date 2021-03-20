package com.monee.graphql;

import com.monee.dao.AccountDao;
import com.monee.graphql.DataFetcher.account.AccountDataFetcher;
import com.monee.graphql.DataFetcher.account.AllAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.CreateAccountDataFetcher;
import com.monee.graphql.DataFetcher.account.UpdateAccountDataFetcher;
import com.monee.graphql.DataFetcher.post.AllPostDataFetcher;
import com.monee.graphql.DataFetcher.post.CreatePostDataFetcher;
import com.monee.graphql.DataFetcher.post.DeletePostDataFetcher;
import com.monee.graphql.DataFetcher.post.LikePostDataFetcher;
import com.monee.graphql.DataFetcher.post.PostDataFetcher;
import com.monee.graphql.DataFetcher.post.UpdatePostDataFetcher;
import com.monee.graphql.DataFetcher.reply.AllReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.CreateReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.DeleteReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.ReplyDataFetcher;
import com.monee.graphql.DataFetcher.reply.UpdateReplyDataFetcher;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;


public class ServiceGraphQLProvider implements ServiceGraphQL {

    private static Logger log = LoggerFactory.getLogger(ServiceGraphQLProvider.class);
    
    private final AccountDao accountDao;
    private final AllAccountDataFetcher allAccountDataFetcher;
    private final AccountDataFetcher accountDataFetcher;
    private final CreateAccountDataFetcher createAccountDataFetcher;
    private final UpdateAccountDataFetcher updateAccountDataFetcher;
    private final PostDataFetcher postDataFetcher;
    private final AllPostDataFetcher allPostDataFetcher;
    private final CreatePostDataFetcher createPostDataFetcher;
    private final UpdatePostDataFetcher updatePostDataFetcher;
    private final DeletePostDataFetcher deletePostDataFetcher;
    private final AllReplyDataFetcher allReplyDataFetcher;
    private final CreateReplyDataFetcher createReplyDataFetcher;
    private final DeleteReplyDataFetcher deleteReplyDataFetcher;
    private final ReplyDataFetcher replyDataFetcher;
    private final UpdateReplyDataFetcher updateReplyDataFetcher;
    private final LikePostDataFetcher likePostDataFetcher;
    private GraphQL graphQL;

    public ServiceGraphQLProvider(AccountDao accountDao, AllAccountDataFetcher allAccountDataFetcher, AccountDataFetcher accountDataFetcher, CreateAccountDataFetcher createAccountDataFetcher, UpdateAccountDataFetcher updateAccountDataFetcher, PostDataFetcher postDataFetcher, AllPostDataFetcher allPostDataFetcher, CreatePostDataFetcher createPostDataFetcher, UpdatePostDataFetcher updatePostDataFetcher, DeletePostDataFetcher deletePostDataFetcher, AllReplyDataFetcher allReplyDataFetcher, CreateReplyDataFetcher createReplyDataFetcher, DeleteReplyDataFetcher deleteReplyDataFetcher, ReplyDataFetcher replyDataFetcher, UpdateReplyDataFetcher updateReplyDataFetcher, LikePostDataFetcher likePostDataFetcher) {
        this.accountDao = accountDao;
        this.allAccountDataFetcher = allAccountDataFetcher;
        this.accountDataFetcher = accountDataFetcher;
        this.createAccountDataFetcher = createAccountDataFetcher;
        this.updateAccountDataFetcher = updateAccountDataFetcher;
        this.postDataFetcher = postDataFetcher;
        this.allPostDataFetcher = allPostDataFetcher;
        this.createPostDataFetcher = createPostDataFetcher;
        this.updatePostDataFetcher = updatePostDataFetcher;
        this.deletePostDataFetcher = deletePostDataFetcher;
        this.allReplyDataFetcher = allReplyDataFetcher;
        this.createReplyDataFetcher = createReplyDataFetcher;
        this.deleteReplyDataFetcher = deleteReplyDataFetcher;
        this.replyDataFetcher = replyDataFetcher;
        this.updateReplyDataFetcher = updateReplyDataFetcher;
        this.likePostDataFetcher = likePostDataFetcher;
    }

    public ExecutionResult execute(String query){
        log.info("AccountServiceGraphQLProvider execute");
        return graphQL.execute(query);
    }

    public void loadSchema() throws IOException {

        log.info("AccountServiceGraphQLProvider load schema");

        try{

            File schemaFile = new File("C:\\Users\\kjuio\\IdeaProjects\\monee_lab_assignment\\src\\main\\resources\\graphql\\" + "schema.graphqls");

            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);

            RuntimeWiring runtimeWiring = newRuntimeWiring()
                    .type("Query", builder -> builder
                            .dataFetcher("allAccounts",allAccountDataFetcher)
                            .dataFetcher("Account",accountDataFetcher)
                            .dataFetcher("Post",postDataFetcher)
                            .dataFetcher("allPosts",allPostDataFetcher)
                            .dataFetcher("Reply",replyDataFetcher)
                            .dataFetcher("allReplys",allReplyDataFetcher)
                    )
                    .type("Mutation", builder -> builder
                            .dataFetcher("updateAccount", updateAccountDataFetcher)
                            .dataFetcher("createAccount",createAccountDataFetcher)
                            .dataFetcher("createPost",createPostDataFetcher)
                            .dataFetcher("updatePost",updatePostDataFetcher)
                            .dataFetcher("deletePost",deletePostDataFetcher)
                            .dataFetcher("createReply",createReplyDataFetcher)
                            .dataFetcher("updateReply",updateReplyDataFetcher)
                            .dataFetcher("deleteReply",deleteReplyDataFetcher)
                            .dataFetcher("addLikePost",likePostDataFetcher)
                    )
                    .build();
            SchemaGenerator schemaGenerator = new SchemaGenerator();
            GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);

            GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

            this.graphQL = build;
        } catch(Exception e) {
            e.printStackTrace();
            log.info("스키마 오류");
        }
    }

}
