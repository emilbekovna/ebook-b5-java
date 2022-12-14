package kg.eBook.ebookb5.apis;

import kg.eBook.ebookb5.dto.requests.LoginRequest;
import kg.eBook.ebookb5.dto.requests.UserRegisterRequest;
import kg.eBook.ebookb5.dto.requests.VendorRegisterRequest;
import kg.eBook.ebookb5.dto.responses.JwtResponse;
import kg.eBook.ebookb5.exceptions.WrongEmailException;
import kg.eBook.ebookb5.services.LoginService;
import kg.eBook.ebookb5.services.UserService;
import kg.eBook.ebookb5.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthApi {

    private final VendorService vendorService;
    private final UserService personService;
    private final LoginService loginService;

    @PostMapping("/vendor/register")
    public JwtResponse registrationVendor(@RequestBody @Valid VendorRegisterRequest vendorRegisterRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new WrongEmailException("Неправильный адрес электронной почты");
        }
        return vendorService.registerVendor(vendorRegisterRequest);
    }

    @PostMapping("/user/register")
    public JwtResponse registrationPerson(@RequestBody @Valid UserRegisterRequest personRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new WrongEmailException("Неправильный адрес электронной почты");
        }
        return personService.registerUser(personRequest);
    }

    @PostMapping("/login")
    public JwtResponse performLogin(@RequestBody LoginRequest loginResponse) {
       return loginService.authenticate(loginResponse);
    }
}


