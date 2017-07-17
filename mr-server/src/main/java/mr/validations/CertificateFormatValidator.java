package mr.validations;

import org.everit.json.schema.FormatValidator;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Optional;

public class CertificateFormatValidator implements FormatValidator {

    private CertificateFactory certificateFactory;

    public CertificateFormatValidator() {
        try {
            this.certificateFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new IllegalArgumentException("X.509 not supported");
        }
    }

    @Override
    public Optional<String> validate(String certificate) {
        String wrappedCert = wrapCert(certificate);
        try {
            certificateFactory.generateCertificate(new ByteArrayInputStream(wrappedCert.getBytes()));
        } catch (CertificateException e) {
            return Optional.of("Invalid certificate");
        }
        return Optional.empty();
    }

    @Override
    public String formatName() {
        return "certificate";
    }

    private String wrapCert(String certificate) {
        return "-----BEGIN CERTIFICATE-----\n" + certificate + "\n-----END CERTIFICATE-----";
    }
}