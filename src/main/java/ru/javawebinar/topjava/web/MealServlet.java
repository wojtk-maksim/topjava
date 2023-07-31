package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealDao dao;

    @Override
    public void init() {
        dao = new MealDaoInMemory();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        if ("edit".equals(action)) {
            if (id == null) {
                log.debug("redirect to new meal form");
            } else {
                log.debug("redirect to update meal form [id: {}]", id);

                Meal meal = dao.get(Integer.parseInt(id));
                request.setAttribute("meal", meal);
            }
            request.getRequestDispatcher("editMeal.jsp").forward(request, response);
        } else {
            log.debug("redirect to meals");

            request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            String dateTime = request.getParameter("dateTime");
            String description = request.getParameter("description");
            String calories = request.getParameter("calories");
            if (id == null || id.isEmpty()) {
                log.debug("save new meal [dateTime: {}, description: {}, calories: {}]", dateTime, description, calories);

                Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
                dao.save(meal);
            } else {
                log.debug("update meal [id: {}, dateTime: {}, description: {}, calories: {}]", id, dateTime, description, calories);

                Meal meal = new Meal(Integer.parseInt(id), LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
                dao.save(meal);
            }
        } else if ("delete".equals(action)) {
            log.debug("delete meal [id: {}]", id);

            dao.delete(Integer.parseInt(id));
        }

        response.sendRedirect("meals");
    }
}
