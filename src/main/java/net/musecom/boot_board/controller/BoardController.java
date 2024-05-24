package net.musecom.boot_board.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import net.musecom.boot_board.dto.BoardDto;
import net.musecom.boot_board.service.BoardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    
    private final BoardService bService;

    @GetMapping("/")
    public String getList(Model model) {
        System.out.println("list");
        List<BoardDto> bDtoList = bService.findAll();
        model.addAttribute("lists", bDtoList);
        return "list";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDto> boardList = bService.paging(pageable);
        return "paging";
    }

 
    @GetMapping("/{id}")
    public String detailView(@PathVariable Long id, Model model) {
        /**
         * 로직 
         * 1.조회수(hit)를 1 올리고 
         * 2.detial.html 을 출력
         */
        bService.updateHits(id);
        BoardDto bDto = bService.findById(id);
        model.addAttribute("board", bDto); 
        return "detail";
    }

    @GetMapping("/write")
    public String getWrite() {
        System.out.println("write");
        return "write";
    }
 
    @PostMapping("/write")
    public String setWrite(@ModelAttribute BoardDto bDto) {
        System.out.println("boardDto = " + bDto);
        bService.write(bDto);
        return "redirect:/board/";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDto boardDto = bService.findById(id);
        model.addAttribute("board", boardDto);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDto bDto, Model model, RedirectAttributes redirectAttributes){
        //비밀번호 검증을 위해 bDto에서 받을 비번과 boardDto에 담겨있는 비번을 비교한다.
        BoardDto boardDto = bService.findById(bDto.getId());
        if(boardDto.getPass().equals(bDto.getPass())){
            //수정로직 처리
            BoardDto board = bService.update(bDto);
            model.addAttribute("board", board);
            return "detail";
        }else{
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/board/update/" + bDto.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
      
        model.addAttribute("id", id);
        return "deleteForm";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam String pass, RedirectAttributes redirectAttributes, Model model) {
        //1. pass에 값이 있는지?
        if(pass==null || pass.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "비밀번호를 입력하세요.");
            return "redirect:delete/" + id;
        }
        //2. id 와 pass 가 db 같은지 ?
        BoardDto boardDto = bService.findById(id);
        if(boardDto != null && boardDto.getPass().equals(pass)){
            bService.delete(id);
            return "redirect:/board/";
        }else{
            redirectAttributes.addFlashAttribute("error", "비밀번호가 틀렸습니다.");
            return "redirect:delete/" + id;            
        }  
    }
    
}