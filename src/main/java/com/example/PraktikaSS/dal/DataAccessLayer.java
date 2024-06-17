package com.example.PraktikaSS.dal;


import com.example.PraktikaSS.models.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Getter
public class DataAccessLayer {
    private final SessionFactory sessionFactory;

    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Session session = null;
    public void createProduct(Product newProduct) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newProduct);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteProductById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product product = session.get(Product.class, id);
        session.remove(product);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updateProductById(Long id, Product updatedProduct){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product product = session.get(Product.class, id);
        product.setCompany(updatedProduct.getCompany());
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        session.merge(product);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public Product getProductById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Product product = session.get(Product.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return product;
    }
    public List<Product> getProducts(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);
        List<Product> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public void createUser(User user){
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteUserById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.remove(id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updateUser(Long id, User updatedUser){
        session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        session.merge(user);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public User getUserById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return user;
    }
    public List<User> getUsers(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        List<User> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public String newUserToDatabase(User user) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String name = user.getUserName();

        Query query = session
                .createQuery("FROM User where userName = :username")
                .setParameter("username", name);
        User userFrom = (User) query.uniqueResult();

        if (userFrom != null) {
            return "Выберите другое имя";
        }
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return "Pabeda)";
    }
    public User getUserFromDatabaseByUsername(String name) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session
                .createQuery("FROM User where userName = :username")
                .setParameter("username", name);
        User userFrom = (User) query.uniqueResult();
        if (userFrom == null) {
            return null;
        }
        return userFrom;
    }
    public void createBasket(Basket basket){
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(basket);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteBasketsByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Basket> basketsToDelete = session.createQuery("SELECT b FROM Basket b WHERE b.user.id = :userId", Basket.class)
                .setParameter("userId", userId)
                .getResultList();
        for (Basket basket : basketsToDelete) {
            session.remove(basket);
        }
        session.getTransaction().commit();
        session.close();
        }
    public List<Basket> getBaskets(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Basket> query = builder.createQuery(Basket.class);
        Root<Basket> root = query.from(Basket.class);
        query.select(root);
        List<Basket> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public List<Basket> getBasketsByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.createQuery("SELECT b FROM Basket b WHERE b.user.id = :userId", Basket.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public void createDiscount(Discount newDiscount) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(newDiscount);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteDiscountById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Discount discount = session.get(Discount.class, id);
        session.remove(id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updateDiscount(Long id, Discount updatedDiscount){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Discount discount = session.get(Discount.class, id);
        discount.setProduct(updatedDiscount.getProduct());
        discount.setPercent(updatedDiscount.getPercent());
        session.merge(discount);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public Discount getDiscountById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Discount discount = session.get(Discount.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return discount;
    }
    public List<Discount> getDiscounts(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Discount> query = builder.createQuery(Discount.class);
        Root<Discount> root = query.from(Discount.class);
        query.select(root);
        List<Discount> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
public Order createOrderWithBasketItems(Long userId) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    Order order = new Order();
    order.setStatus(false);
    List<Basket> baskets = session.createQuery("SELECT b FROM Basket b WHERE b.user.id = :userId", Basket.class)
            .setParameter("userId", userId)
            .getResultList();
    for (Basket basket : baskets) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(basket.getProduct());
        order.getOrderItems().add(orderItem);
    }
    session.save(order);
    for (Basket basket : baskets) {
        session.remove(basket);
    }
    session.getTransaction().commit();
    session.close();
    return order;
}
    public void deleteOrderById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Order order = session.get(Order.class, id);
        session.remove(id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public Order getOrderById(Long orderId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId", Order.class)
                    .setParameter("orderId", orderId)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
    public List<Order> getOrders(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        List<Order> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public void createPoint(Point point){
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(point);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deletePointById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Point point = session.get(Point.class, id);
        session.remove(id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updatePoint(Long id, Point updatedPoint){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Point point = session.get(Point.class, id);
        point.setAddress(updatedPoint.getAddress());
        point.setEmployeeQuantity(updatedPoint.getEmployeeQuantity());
        point.setPhoneNumber(updatedPoint.getPhoneNumber());
        session.merge(point);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public Point getPointById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Point point = session.get(Point.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return point;
    }
    public List<Point> getPoints(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Point> query = builder.createQuery(Point.class);
        Root<Point> root = query.from(Point.class);
        query.select(root);
        List<Point> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public void createReview(Review review){
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(review);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteReviewById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Review review = session.get(Review.class, id);
        session.remove(id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public Review getReviewById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Review review = session.get(Review.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return review;
    }
    public List<Review> getReviews(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Review> query = builder.createQuery(Review.class);
        Root<Review> root = query.from(Review.class);
        query.select(root);
        List<Review> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
}