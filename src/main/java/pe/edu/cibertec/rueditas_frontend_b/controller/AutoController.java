package pe.edu.cibertec.rueditas_frontend_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_frontend_b.dto.AutoRequest;
import pe.edu.cibertec.rueditas_frontend_b.dto.AutoResponse;
import pe.edu.cibertec.rueditas_frontend_b.viewmodel.AutoModel;

@Controller
@RequestMapping("/auto")
public class AutoController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String getBuscarAuto(Model model) {

        AutoModel autoModel = new AutoModel(
                "00",
                "",
                "",
                "",
                "",
                "",
                "");
        model.addAttribute("autoModel", autoModel);
        return "buscar";


    }

    @PostMapping("/buscar")
    public String postBuscarAuto(

        @RequestParam("placa") String placa,
        Model model) {
        if (placa == null || placa.trim().isEmpty() || placa.length() != 7) {

            AutoModel autoModel = new AutoModel(
                    "01",
                    "La placa ingresado no es valido.",
                    "", "", "", "", "");
            model.addAttribute("autoModel", autoModel);
            return "buscar";

        }

        try {

            String endpoint = "http://localhost:8080/api/vehiculos";
            AutoRequest autoRequest = new AutoRequest(placa);
            AutoResponse autoResponse = restTemplate.postForObject(endpoint, autoRequest, AutoResponse.class);

            if (autoResponse.codigo().equals("00")) {

                AutoModel autoModel = new AutoModel(
                        "00", "",
                        autoResponse.autoMarca(),
                        autoResponse.autoModelo(),
                        autoResponse.autoNroAsientos(),
                        autoResponse.autoPrecio(),
                        autoResponse.autoColor());
                model.addAttribute("autoModel", autoModel);
                return "detalle";

            } else {

                AutoModel autoModel = new AutoModel(
                        "01",
                        "No se encontró un vehículo para la placa ingresado.",
                        "", "", "", "", "");
                model.addAttribute("autoModel", autoModel);
                return "buscar";

            }
        } catch(Exception e) {

            AutoModel autoModel = new AutoModel(
                    "99",
                    "Error: Ocurrió un problema al buscar el vehículo.",
                    "", "", "", "", "");

            model.addAttribute("autoModel", autoModel);
            System.out.println(e.getMessage());
            return "buscar";

        }
    }


}
