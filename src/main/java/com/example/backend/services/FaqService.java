package com.example.backend.services;

import com.example.backend.entity.Faq;
import com.example.backend.repositories.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaqService {
    @Autowired
    private FaqRepository faqRepository;

    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    public Optional<Faq> getFaqById(Long id) {
        return faqRepository.findById(id);
    }

    public Faq createFaq(Faq faq) {
        return faqRepository.save(faq);
    }

    public Faq updateFaq(Long id, Faq faqDetails) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id " + id));
        faq.setQuestion(faqDetails.getQuestion());
        faq.setAnswer(faqDetails.getAnswer());
        return faqRepository.save(faq);
    }

    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found with id " + id));
        faqRepository.delete(faq);
    }
}