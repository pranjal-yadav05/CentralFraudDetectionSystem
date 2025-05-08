package com.example.neo4jplayground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class DataGeneratorService {

    private static final Logger logger = Logger.getLogger(DataGeneratorService.class.getName());
    private final Neo4jClient neo4jClient;

    @Autowired
    public DataGeneratorService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    /**
     * Ensure projection exists, or create it.
     */
    private void projectGraph() {
        try {
            neo4jClient.query(
                    "CALL gds.graph.project('account_network', " +
                            "'Account', { " +
                            "  TRANSACTS_WITH: { " +
                            "    orientation: 'NATURAL', " +
                            "    properties: ['amt'] " +
                            "  } " +
                            "})"
            ).run();
            logger.info("Graph projection created.");
        } catch (Exception e) {
            logger.info("Graph may already be projected: " + e.getMessage());
        }
    }

    public void exportToCsv(List<Map<String, Object>> results, String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Write headers
            Set<String> headers = results.get(0).keySet();
            writer.println(String.join(",", headers));

            // Write each row
            for (Map<String, Object> row : results) {
                List<String> values = headers.stream()
                        .map(header -> Optional.ofNullable(row.get(header)).orElse("").toString())
                        .map(value -> value.replace(",", ";")) // replace commas to keep CSV format clean
                        .toList();
                writer.println(String.join(",", values));
            }

            System.out.println("CSV export completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Transactional
    public void runAllAlgorithms() {
        try {
         //  projectGraph();

            // PageRank
            neo4jClient.query(
                    "CALL gds.pageRank.write(\n" +
                            "    'account_network',\n" +
                            "    {\n" +
                            "        maxIterations: 20,\n" +
                            "        dampingFactor: 0.85,\n" +
                            "        writeProperty: 'pagerank'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;\n"
            ).run();
            logger.info("PageRank written.");



            // Betweenness Centrality
            neo4jClient.query(
                    "CALL gds.betweenness.write(\n" +
                            "    'account_network',\n" +
                            "    {\n" +
                            "        writeProperty: 'betweenness'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;"
            ).run();
            logger.info("Betweenness Centrality written.");


            // Weakly Connected Components
            neo4jClient.query(
                    "CALL gds.wcc.write('transactionGraph', {" +
                            " writeProperty: 'componentId'" +
                            "})"
            ).run();
            logger.info("Weakly Connected Components written.");

            // Louvain Community Detection
            neo4jClient.query(
                    "CALL gds.louvain.write('transactionGraph', {" +
                            " writeProperty: 'communityId'" +
                            "})"
            ).run();
            logger.info("Louvain Communities written.");


            neo4jClient.query(
                    "CALL gds.degree.write(\n" +
                            "    'account_network',\n" +
                            "    {\n" +
                            "        writeProperty: 'degree'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;"
            ).run();
            logger.info("Degree written.");

            neo4jClient.query(
                    "CALL gds.degree.write(\n" +
                            "    'account_network',\n" +
                            "    {\n" +
                            "        orientation: 'REVERSE',\n" +
                            "        writeProperty: 'in_degree'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;"
            ).run();
            logger.info("InDegree written.");

            neo4jClient.query(
                    "CALL gds.degree.write(\n" +
                            "    'account_network',\n" +
                            "    {\n" +
                            "        orientation: 'NATURAL',\n" +
                            "        writeProperty: 'out_degree'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;"
            ).run();
            logger.info("Out degree written.");

            neo4jClient.query("// First count how many accounts are in each community\n" +
                    "MATCH (a:Account)\n" +
                    "WHERE a.communityId IS NOT NULL\n" +
                    "WITH a.communityId AS community, count(*) AS size\n" +
                    "\n" +
                    "// Then update all accounts in each community with that size\n" +
                    "MATCH (a:Account)\n" +
                    "WHERE a.communityId = community\n" +
                    "SET a.community_size = size").run();



            neo4jClient.query(
                    "CALL gds.graph.project(\n" +
                            "    'account_network_undirected',\n" +
                            "    'Account',\n" +
                            "    {\n" +
                            "        TRANSACTS_WITH: {\n" +
                            "            orientation: 'UNDIRECTED'\n" +
                            "        }\n" +
                            "    }\n" +
                            ");"
            ).run();
            logger.info("Undireacted graph.");


            neo4jClient.query(
                    "CALL gds.triangleCount.write(\n" +
                            "    'account_network_undirected',\n" +
                            "    {\n" +
                            "        writeProperty: 'triangle_count'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;"
            ).run();
            logger.info("Triangle count written.");


            neo4jClient.query(
                    "CALL gds.localClusteringCoefficient.write(\n" +
                            "    'account_network_undirected',\n" +
                            "    {\n" +
                            "        writeProperty: 'clustering_coefficient'\n" +
                            "    }\n" +
                            ")\n" +
                            "YIELD nodePropertiesWritten;\n" +
                            "\n"
            ).run();
            neo4jClient.query(
                            "MATCH (a:Account)-[:TRANSACTS_WITH]->(:Account)-[:TRANSACTS_WITH]->(:Account)-[:TRANSACTS_WITH]->(a)\n" +
                            "WITH a, count(*) AS cycles3\n" +
                            "SET a.cycle_count = cycles3;\n" +
                            "\n" ).run();

            neo4jClient.query(
                            "MATCH path = (a:Account)-[:TRANSACTS_WITH]->(:Account)-[:TRANSACTS_WITH]->(:Account)-[:TRANSACTS_WITH]->(:Account)-[:TRANSACTS_WITH]->(a)\n" +
                            "WHERE length(path) = 5\n" +
                            "WITH a, count(*) AS cycles4\n" +
                            "SET a.cycle_count = COALESCE(a.cycle_count, 0) + cycles4;\n" ).run();

                           neo4jClient.query(
                            "MATCH (a:Account)\n" +
                            "WHERE a.cycle_count IS NULL\n" +
                            "SET a.cycle_count = 0;").run();


            logger.info("cycle count written.");


            neo4jClient.query(
                    "MATCH (a:Account)\n" +
                            "WHERE a.in_degree > 0 AND a.out_degree > 0\n" +
                            "SET a.intermediate_accounts = 1;\n"
            ).run();

            neo4jClient.query(
                            "MATCH (a:Account)\n" +
                            "WHERE NOT (a.in_degree > 0 AND a.out_degree > 0)\n" +
                            "SET a.intermediate_accounts = 0;\n").run();

            logger.info("non-intermediate accounts written.");

            neo4jClient.query(
                    "MATCH (a:Account)\n" +
                            "WITH a,\n" +
                            "     // Normalize metrics (capped at 1.0)\n" +
                            "     CASE WHEN a.pagerank > 0.1 THEN 0.1 ELSE a.pagerank END / 0.1 AS norm_pagerank,\n" +
                            "     CASE WHEN a.betweenness > 0.1 THEN 0.1 ELSE a.betweenness END / 0.1 AS norm_betweenness,\n" +
                            "     CASE WHEN a.degree > 20 THEN 1.0 ELSE a.degree / 20.0 END AS norm_degree,\n" +
                            "     CASE WHEN a.triangle_count > 10 THEN 1.0 ELSE a.triangle_count / 10.0 END AS norm_triangles,\n" +
                            "     CASE WHEN a.cycle_count > 5 THEN 1.0 ELSE a.cycle_count / 5.0 END AS norm_cycles,\n" +
                            "     a.intermediate_accounts AS is_intermediate,\n" +
                            "     // Check for imbalanced in/out degree\n" +
                            "     ABS((1.0 * a.in_degree / (a.in_degree + a.out_degree + 0.001)) - 0.5) * 2 AS degree_imbalance\n" +
                            "\n" +
                            "// Calculate weighted fraud score\n" +
                            "WITH a,\n" +
                            "     norm_pagerank * 0.15 +\n" +
                            "     norm_betweenness * 0.20 +\n" +
                            "     norm_degree * 0.15 +\n" +
                            "     norm_triangles * 0.10 +\n" +
                            "     norm_cycles * 0.20 +\n" +
                            "     is_intermediate * 0.10 +\n" +
                            "     degree_imbalance * 0.10 AS raw_score\n" +
                            "\n" +
                            "// Cap between 0 and 1, and write back to node\n" +
                            "SET a.fraud_score = CASE \n" +
                            "    WHEN raw_score > 1.0 THEN 1.0\n" +
                            "    WHEN raw_score < 0.0 THEN 0.0\n" +
                            "    ELSE raw_score\n" +
                            "END;"
            ).run();

            logger.info("Fruad scores written.");

            neo4jClient.query(
                    "MATCH (a:Account)\n" +
                            "WITH a.fraud_score AS score, a\n" +
                            "ORDER BY score DESC\n" +
                            "WITH collect(a) as sorted_accounts\n" +
                            "WITH sorted_accounts, size(sorted_accounts) * 0.03 AS threshold // Label top 3% as fraudulent\n" +
                            "UNWIND range(0, size(sorted_accounts)-1) AS idx\n" +
                            "WITH sorted_accounts[idx] AS account, idx < threshold AS is_fraud\n" +
                            "SET account.is_fraud = CASE WHEN is_fraud THEN 1 ELSE 0 END;"
            ).run();

            neo4jClient.query("CALL gds.graph.drop('account_network');\n")
                    .run();

            neo4jClient.query("CALL gds.graph.drop('account_network_undirected');\n")
                    .run();


            List<Map<String, Object>> results = (List<Map<String, Object>>) neo4jClient
                    .query("\n" +
                            "MATCH (a:Account)\n" +
                            "RETURN \n" +
                            "    a.accountNumber,\n" +
                            "    a.pagerank,\n" +
                            "    a.degree,\n" +
                            "    a.in_degree,\n" +
                            "    a.out_degree, \n" +
                            "    a.betweenness,\n" +
                            "    a.communityId,\n" +
                            "a.community_size,\n"+
                            "    a.triangle_count,\n" +
                            "    a.cycle_count,\n" +
                            "    a.intermediate_accounts,\n" +
                            "    a.is_fraud,\n" +
                            "    a.fraud_score\n" +
                            "ORDER BY a.fraud_score DESC;")
                    .fetch()
                    .all();
            exportToCsv(results, "accounts.csv");



        } catch (Exception e) {
            logger.severe("Error running algorithms: " + e.getMessage());
            throw e;
        }
    }
}
