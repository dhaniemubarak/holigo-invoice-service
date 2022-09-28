package id.holigo.services.holigoinvoiceservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelFasilitasDto {
    private String icon;

    private String category;

    private List<String> items;
}
