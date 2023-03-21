package com.engeto.florist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Florist {
    private static void deleteNonpoisonousItems(Flower[] flowers,
                                                Statement statement,
                                                String table)
            throws SQLException {
        statement.executeUpdate("DELETE FROM " + table
                + " WHERE is_poisonous = false");
        for (int i = 0; i < flowers.length; ++i) {
            if (!flowers[i].isPoisonous()) {
                flowers[i] = null;
            }
        }
    }

    private static void insertItems(Flower[] flowers, Statement statement,
                                    String table) throws SQLException {
        for (Flower flower : flowers) {
            statement.executeUpdate("INSERT INTO " + table
                    + " VALUES " + "(" + flower.getId() + ", '"
                    + flower.getFlowerName() + "', '"
                    + flower.getColor() + "', '"
                    + flower.getDescription() + "', "
                    + flower.isPoisonous()
                    + ")");
        }
    }

    private static void printNames(Statement statement, String table)
            throws SQLException {
        statement.executeQuery("SELECT * FROM " + table);
        ResultSet result = statement.getResultSet();
        while (result.next()) {
            System.out.println(result.getString("flower_name"));
        }
    }

    private static void updateDescription(Flower flower, Statement statement,
                                          String table, String description)
            throws SQLException {
        statement.executeUpdate("UPDATE " + table + " SET "
                + "description = '" + description
                + "' WHERE flower_name = '"
                + flower.getFlowerName() + "'");
        flower.setDescription(description);
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Florist.class.getName());

        Flower snowflake = new Flower("Bledule", "bílá",
                "jarní květina", true);
        Flower marguerite = new Flower("Kopretina", "bíložlutá",
                "letní květina", false);
        Flower[] flowers = {snowflake, marguerite};

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306",
                "mysql.florist",
                "flor54iSt")) {
            Statement statement = connection.createStatement();

            String table = "florist.flower";
            // Clear the table to enable running the program repetitively
            statement.executeUpdate("DELETE FROM " + table);

            insertItems(flowers, statement, table);

            String snowflakeDescriptionUpdate =
                    "Pozor na cibulku - obsahuje největší koncentraci jedu!";
            updateDescription(snowflake, statement, table,
                    snowflakeDescriptionUpdate);

            deleteNonpoisonousItems(flowers, statement, table);

            System.out.println("All flowers in table:");
            printNames(statement, table);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Chyba při komunikaci s databází: "
                    + e.getLocalizedMessage());
        }
    }
}
