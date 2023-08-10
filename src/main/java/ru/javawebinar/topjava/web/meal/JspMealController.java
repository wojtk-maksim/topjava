package ru.javawebinar.topjava.web.meal;

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

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFilteredMeals(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/edit")
    public String showEditForm(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        if (StringUtils.hasLength(id)) {
            model.addAttribute("meal", super.get(Integer.parseInt(id)));
        } else {
            model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        }
        return "mealForm";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal meal = new Meal(
                StringUtils.hasLength(id) ? Integer.parseInt(id) : null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.getId());
        }
        return "redirect:/meals";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(Integer.parseInt(request.getParameter("id")));
        return "redirect:/meals";
    }
}
