package com.gymtrack.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.gymtrack.dto.VoucherDtos.VoucherValidationResponse;
import com.gymtrack.dto.payment.D17ConfigResponse;
import com.gymtrack.model.User;
import com.gymtrack.repository.UserRepository;
import com.gymtrack.service.D17PaymentService;
import com.gymtrack.service.VoucherService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

@Endpoint
public class PaymentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://gymtrack.com/soap/payments";

    private final VoucherService voucherService;
    private final D17PaymentService d17PaymentService;
    private final UserRepository userRepository;

    public PaymentSoapEndpoint(VoucherService voucherService,
                               D17PaymentService d17PaymentService,
                               UserRepository userRepository) {
        this.voucherService = voucherService;
        this.d17PaymentService = d17PaymentService;
        this.userRepository = userRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ValidateVoucherRequest")
    @ResponsePayload
    public Element validateVoucher(@RequestPayload Element request) throws Exception {
        String voucherCode = getTextContent(request, "voucherCode");
        double orderAmount = Double.parseDouble(getTextContent(request, "orderAmount"));

        VoucherValidationResponse result = voucherService.validateVoucher(voucherCode, orderAmount);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element response = doc.createElementNS(NAMESPACE_URI, "ValidateVoucherResponse");

        appendElement(doc, response, "valid", String.valueOf(result.valid()));
        appendElement(doc, response, "message", result.message());
        appendElement(doc, response, "discountType", result.discountType() != null ? result.discountType() : "");
        appendElement(doc, response, "discountValue", String.valueOf(result.discountValue()));
        appendElement(doc, response, "discountAmount", String.valueOf(result.discountAmount()));
        appendElement(doc, response, "finalAmount", String.valueOf(result.finalAmount()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetD17ConfigRequest")
    @ResponsePayload
    public Element getD17Config(@RequestPayload Element request) throws Exception {
        D17ConfigResponse config = d17PaymentService.getConfig();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element response = doc.createElementNS(NAMESPACE_URI, "GetD17ConfigResponse");

        appendElement(doc, response, "phoneNumber", config.phoneNumber());
        appendElement(doc, response, "recipientName", config.recipientName());
        appendElement(doc, response, "instructions", config.instructions());
        appendElement(doc, response, "slaHours", String.valueOf(config.slaHours()));
        appendElement(doc, response, "currency", config.currency());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "VerifyMembershipRequest")
    @ResponsePayload
    public Element verifyMembership(@RequestPayload Element request) throws Exception {
        String userId = getTextContent(request, "userId");

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element response = doc.createElementNS(NAMESPACE_URI, "VerifyMembershipResponse");

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            appendElement(doc, response, "active", "false");
            appendElement(doc, response, "plan", "");
            appendElement(doc, response, "expiresAt", "");
            appendElement(doc, response, "message", "User not found");
        } else {
            boolean hasActivePlan = user.getMembershipTier() != null
                    && !user.getMembershipTier().isBlank()
                    && !"FREE".equalsIgnoreCase(user.getMembershipTier());
            appendElement(doc, response, "active", String.valueOf(hasActivePlan));
            appendElement(doc, response, "plan", user.getMembershipTier() != null ? user.getMembershipTier() : "FREE");
            appendElement(doc, response, "expiresAt",
                    user.getMembershipExpiresAt() != null ? user.getMembershipExpiresAt().toString() : "");
            appendElement(doc, response, "message", hasActivePlan ? "Active membership" : "No active membership");
        }

        return response;
    }

    private String getTextContent(Element parent, String tagName) {
        var nodes = parent.getElementsByTagNameNS(NAMESPACE_URI, tagName);
        if (nodes.getLength() == 0) {
            nodes = parent.getElementsByTagName(tagName);
        }
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent() : "";
    }

    private void appendElement(Document doc, Element parent, String name, String value) {
        Element el = doc.createElementNS(NAMESPACE_URI, name);
        el.setTextContent(value != null ? value : "");
        parent.appendChild(el);
    }
}
