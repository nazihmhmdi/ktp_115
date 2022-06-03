/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unu.jogja.project.ktp.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nazih
 */
@Controller
public class DummyController {

    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();

    @RequestMapping("/dummy")
    //@ResponseBody
    public String getDummy (Model model) {
    int record = dummyController.getDummyCount();
        String result = "";
        
        try{
            data = dummyController.findDummyEntities().subList(0, record);
        }
        catch (Exception e){
            result=e.getMessage();
        }
        
        model.addAttribute("goDummy", data);
        model.addAttribute("record", record);
         
        return "dummy";    
    }
    
    @RequestMapping("/create")
    public String createDummy() {
        return "dummy/create";
    }

    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String newDummy(HttpServletRequest data, @RequestParam("gambar") MultipartFile file) throws ParseException, Exception {

        Dummy dumdata = new Dummy();

        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);

        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);

        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();

        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        
        dummyController.create(dumdata);

        return "dummy/create";
    }
    
    @RequestMapping (value="/image" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
	Dummy dumdata = dummyController.findDummy(id);
	byte[] image = dumdata.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
    
    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deleteDummy(@PathVariable("id") int id) throws Exception {
        dummyController.destroy(id);
        return "deleted";
    }

    @RequestMapping("/edit/{id}")
    public String updateDummy(@PathVariable("id") int id, Model m) throws Exception {
        Dummy d = dummyController.findDummy(id);
        m.addAttribute("data", d);
        return "dummy/update";
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String updateDummyData(@RequestParam("gambar") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Dummy d = new Dummy();

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tanggal"));
        byte[] img = f.getBytes();
        d.setId(id);
        d.setTanggal(date);
        d.setGambar(img);

        dummyController.edit(d);
        return "updated";
    }
}
