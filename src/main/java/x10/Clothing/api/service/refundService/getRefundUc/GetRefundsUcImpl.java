package x10.Clothing.api.service.refundService.getRefundUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IRefundRepository;
import x10.Clothing.api.service.refundService.RefundResponse;
import x10.Clothing.api.service.refundService.RefundResponseMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRefundsUcImpl implements IGetRefundsUc {

    private final IRefundRepository refundRepository;

    @Override
    public List<RefundResponse> getMyRefunds(String userId) {
        return refundRepository.findByUserId(userId).stream()
                .map(RefundResponseMapper::toResponse)
                .toList();
    }

    @Override
    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(RefundResponseMapper::toResponse)
                .toList();
    }
}
