package com.engeto.florist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Florist {
    private static void deleteAll(Statement statement, String table)
            throws SQLException {
        statement.executeUpdate("DELETE FROM " + table + ";");
    }

    private static void deleteNonpoisonousItems(Statement statement,
                                                String table)
            throws SQLException {
        statement.executeUpdate("DELETE FROM " + table
                + " WHERE is_poisonous = false;");
    }

    private static void insertItem(Flower flower, Statement statement,
                                   String table) throws SQLException {
        statement.executeUpdate("INSERT INTO " + table
                + " VALUES " + "(" + flower.getId() + ", '"
                + flower.getFlowerName() + "', '"
                + flower.getColor() + "', '"
                + flower.getDescription() + "', "
                + flower.isPoisonous()
                + ");");
    }

    private static void printNames(Statement statement, String table)
            throws SQLException {
        statement.executeQuery("SELECT * FROM " + table + ";");
        ResultSet result = statement.getResultSet();
        while (result.next()) {
            System.out.println(result.getString("flower_name"));
        }
    }

    private static void updateDescription(String flowerName,
                                          Statement statement,
                                          String table,
                                          String description)
            throws SQLException {
        statement.executeUpdate("UPDATE " + table + " SET "
                + "description = '" + description
                + "' WHERE flower_name = '"
                + flowerName + "';");
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Florist.class.getName());

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306",
                "mysql.florist",
                "flor54iSt")) {
            Statement statement = connection.createStatement();

            String table = "florist.flower";
            // Clear the table to enable running the program repetitively
            deleteAll(statement, table);

            Flower flower = new Flower("bledule", "bílá",
                    "jarní květina", true);
            insertItem(flower, statement, table);
            flower = new Flower("kopretina", "bíložlutá",
                    "letní květina", false);
            insertItem(flower, statement, table);

            String newDescription =
                    "pozor na cibulku - obsahuje největší koncentraci jedu!";
            updateDescription("bledule", statement, table,
                    newDescription);

            deleteNonpoisonousItems(statement, table);

            System.out.println("Vypiš jména všech květin v tabulce:");
            printNames(statement, table);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Chyba při komunikaci s databází: "
                    + e.getLocalizedMessage());
        }
    }
}
