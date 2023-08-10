package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping
    public String getAll(Model model) {
        log.info("show all for user {}", authUserId());
        model.addAttribute("meals", getTos(service.getAll(authUserId()), authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/filter")
    public String getFilteredMeals(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("show filtered for user {} [startDate: {}, endDate: {}, startTime: {}, endTime: {}]", authUserId(), startDate, endDate, startTime, endTime);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, authUserId());
        model.addAttribute("meals", getFilteredTos(meals, authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    @GetMapping("/edit")
    public String showEditForm(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        if (StringUtils.hasLength(id)) {
            log.info("show update form for {} for user {}", id, authUserId());
            model.addAttribute("meal", service.get(Integer.parseInt(id), authUserId()));
        } else {
            log.info("show create form for user {}", authUserId());
            model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        }
        return "mealForm";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request) {
        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(StringUtils.hasLength(id) ? Integer.parseInt(id) : null, dateTime, description, calories);
        if (meal.isNew()) {
            log.info("create {} for user {}", meal, authUserId());
            service.create(meal, authUserId());
        } else {
            log.info("update {} for user {}", meal, authUserId());
            service.update(meal, authUserId());
        }
        return "redirect:/meals";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("delete {} for user {}", id, authUserId());
        service.delete(id, authUserId());
        return "redirect:/meals";
    }
}
