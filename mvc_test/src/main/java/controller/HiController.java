package controller;

import strereotype.Controller ;
import web.bind.annotation.RequestMapping;
import web.bind.annotation.RequestParam;
import web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/a")
public class HiController {



    @RequestMapping(path = "/b")
    @ResponseBody
    public Pojo say(@RequestParam(name = "id") String id) {
        Pojo pojo=new Pojo();
        pojo.setName(id);
        return pojo;
    }
}
