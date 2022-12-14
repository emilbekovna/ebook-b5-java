package kg.eBook.ebookb5.apis;

import io.swagger.v3.oas.annotations.Operation;
import kg.eBook.ebookb5.dto.requests.VendorProfileRequest;
import kg.eBook.ebookb5.dto.responses.SimpleResponse;
import kg.eBook.ebookb5.dto.responses.VendorResponse;
import kg.eBook.ebookb5.dto.responses.ABookVendorResponse;
import kg.eBook.ebookb5.enums.AboutBooks;
import kg.eBook.ebookb5.exceptions.WrongEmailException;
import kg.eBook.ebookb5.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vendors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VendorApi {

    private final VendorService vendorService;

    @PutMapping("/vendor")
    @PreAuthorize("hasAuthority('VENDOR')")
    @Operation(summary = "update by vendor")
    public VendorResponse update(Authentication authentication,
                                 @RequestBody @Valid VendorProfileRequest vendorProfileRequest,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new WrongEmailException("Неправильный адрес электронной почты");
        }
        return vendorService.update(authentication, vendorProfileRequest);
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "find by vendor with id")
    @PreAuthorize("hasAnyAuthority('VENDOR', 'ADMIN')")
    public VendorResponse findByVendor(@PathVariable Long vendorId) {
        return vendorService.findByVendor(vendorId);
    }

    @DeleteMapping("/{vendorId}")
    @PreAuthorize("hasAnyAuthority('VENDOR', 'ADMIN')")
    @Operation(summary = "delete by vendor with vendor id")
    public SimpleResponse delete(@PathVariable Long vendorId) {
        return vendorService.deleteByVendorId(vendorId);
    }

    @GetMapping("/{vendorId}/books")
    @PreAuthorize("hasAnyAuthority('VENDOR', 'ADMIN')")
    @Operation(summary = "find all books with vendor id", description = "can be obtained using vendor id " +
            "all books, in favorites, in the basket, sold out with discounts in processing and rejected")
    public Page<ABookVendorResponse> findABookVendor(@PathVariable Long vendorId,
                                                     @RequestParam AboutBooks aboutBooks,
                                                     @RequestParam(required = false, defaultValue = "1") int page,
                                                     @RequestParam(required = false, defaultValue = "16") int size) {
        return vendorService.findABookVendor(vendorId, aboutBooks, page, size);
    }
}
