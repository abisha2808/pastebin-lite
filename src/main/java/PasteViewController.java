import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Entity.Paste;
import com.example.demo.Service.PasteService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PasteViewController {
	private final PasteService pasteService;

    public PasteViewController(PasteService pasteService) {
        this.pasteService = pasteService;
    }

    @GetMapping("/p/{id}")
    public String viewPaste(@PathVariable Long id, Model model) {

        Optional<Paste> pasteOpt = pasteService.getPasteForView(id);

        if (pasteOpt.isEmpty()) {
            return "notfound";
        }

        model.addAttribute("paste", pasteOpt.get());
        return "paste";
    }

}
