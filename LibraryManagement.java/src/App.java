import java.sql.*;
import java.util.Scanner;

public class App {

    static final String URL = "jdbc:mysql://127.0.0.1:3306/library_management_system";
    static final String USER = "root";
    static final String PASS = ""; // Set your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✅ Connected to the database!");
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n===== LIBRARY MENU =====");
                System.out.println("1. Add Book");
                System.out.println("2. View Books");
                System.out.println("3. Update Book");
                System.out.println("4. Delete Book");
                System.out.println("5. Add Member");
                System.out.println("6. View Members");
                System.out.println("7. Update Member");
                System.out.println("8. Delete Member");
                System.out.println("9. Issue Book");
                System.out.println("10. View Issued Books");
                System.out.println("11. Return Book");
                System.out.println("12. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addBook(conn, sc);
                    case 2 -> viewBooks(conn);
                    case 3 -> updateBook(conn, sc);
                    case 4 -> deleteBook(conn, sc);
                    case 5 -> addMember(conn, sc);
                    case 6 -> viewMembers(conn);
                    case 7 -> updateMember(conn, sc);
                    case 8 -> deleteMember(conn, sc);
                    case 9 -> issueBook(conn, sc);
                    case 10 -> viewIssuedBooks(conn);
                    case 11 -> returnBook(conn, sc);
                    case 12 -> { System.out.println("👋 Goodbye!"); return; }
                    default -> System.out.println("⚠️ Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------- BOOK METHODS -----------------

    static void addBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Book Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        System.out.print("Year: ");
        int year = sc.nextInt();

        String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.executeUpdate();
            System.out.println("✅ Book added!");
        }
    }

    static void viewBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📚 BOOK LIST:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("title") +
                        " by " + rs.getString("author") +
                        " (" + rs.getInt("year") + ")");
            }
        }
    }

    static void updateBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Book ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Title: ");
        String title = sc.nextLine();
        System.out.print("New Author: ");
        String author = sc.nextLine();
        System.out.print("New Year: ");
        int year = sc.nextInt();

        String sql = "UPDATE books SET title=?, author=?, year=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Book updated!");
            else System.out.println("⚠️ Book not found!");
        }
    }

    static void deleteBook(Connection conn, Scanner sc) throws SQLException {
    System.out.print("Enter Book ID to delete: ");
    int bookId = sc.nextInt();

    // Check if book is issued
    String checkSql = "SELECT * FROM issued_books WHERE book_id=?";
    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        checkStmt.setInt(1, bookId);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            System.out.println("⚠️ Cannot delete: Book is currently issued!");
            return;
        }
    }

    // Delete the book
    String sql = "DELETE FROM books WHERE id=?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, bookId);
        int rows = pstmt.executeUpdate();
        if (rows > 0) System.out.println("✅ Book deleted!");
        else System.out.println("⚠️ Book not found!");
    }
}


    // ----------------- MEMBER METHODS -----------------

    static void addMember(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Member Name: ");
        String name = sc.nextLine();

        String sql = "INSERT INTO members (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("✅ Member added!");
        }
    }

    static void viewMembers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM members";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n👤 MEMBER LIST:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name"));
            }
        }
    }

    static void updateMember(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Member ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();

        String sql = "UPDATE members SET name=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Member updated!");
            else System.out.println("⚠️ Member not found!");
        }
    }

    static void deleteMember(Connection conn, Scanner sc) throws SQLException {
    System.out.print("Enter Member ID to delete: ");
    int memberId = sc.nextInt();

    // Check if member has any issued books
    String checkSql = "SELECT * FROM issued_books WHERE member_id=?";
    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
        checkStmt.setInt(1, memberId);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            System.out.println("⚠️ Cannot delete: Member has issued books!");
            return;
        }
    }

    // Delete the member
    String sql = "DELETE FROM members WHERE id=?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, memberId);
        int rows = pstmt.executeUpdate();
        if (rows > 0) System.out.println("✅ Member deleted!");
        else System.out.println("⚠️ Member not found!");
    }
}


    // ----------------- ISSUED BOOK METHODS -----------------

    static void issueBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Member ID: ");
        int memberId = sc.nextInt();
        System.out.print("Book ID: ");
        int bookId = sc.nextInt();
        sc.nextLine();

        // Check if book is already issued
        String checkSql = "SELECT * FROM issued_books WHERE book_id=?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("⚠️ Book already issued!");
                return;
            }
        }

        String sql = "INSERT INTO issued_books (book_id, member_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            System.out.println("✅ Book issued!");
        }
    }

    static void viewIssuedBooks(Connection conn) throws SQLException {
        String sql = "SELECT ib.id, b.title, m.name, ib.issue_date " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.id " +
                     "JOIN members m ON ib.member_id = m.id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📖 ISSUED BOOKS:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " +
                        rs.getString("title") + " -> " +
                        rs.getString("name") +
                        " (Issued: " + rs.getDate("issue_date") + ")");
            }
        }
    }

    static void returnBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Issued Book ID to return: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM issued_books WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Book returned!");
            else System.out.println("⚠️ Issued record not found!");
        }
    }
}
