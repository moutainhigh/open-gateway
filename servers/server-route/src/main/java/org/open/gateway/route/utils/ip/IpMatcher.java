package org.open.gateway.route.utils.ip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class IpMatcher {

    private final Collection<String> requiredIps;
    private final List<IpRange> sortedIpList = new ArrayList<>();

    public IpMatcher(Collection<String> requiredIps) {
        this.requiredIps = requiredIps;
        init();
    }

    public void init() {
        if (requiredIps.isEmpty()) {
            return;
        }

        List<IpRange> tempIpList = requiredIps.stream()
                .map(IpRange::new)
                .sorted((o1, o2) -> {
                    if (o1.getFrom() != o2.getFrom()) {
                        return o1.getFrom() > o2.getFrom() ? 1 : -1;
                    }
                    if (o1.getTo() != o2.getTo()) {
                        return o1.getTo() > o2.getTo() ? 1 : -1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        int insertIndex = 0;
        for (IpRange ipRange : tempIpList) {
            if (sortedIpList.isEmpty()) {
                sortedIpList.add(ipRange);
                continue;
            }

            IpRange beforeIpRange = sortedIpList.get(insertIndex);

            if (ipRange.getFrom() <= beforeIpRange.getTo() + 1) {
                beforeIpRange.setTo(ipRange.getTo());
            } else {
                sortedIpList.add(ipRange);
                ++insertIndex;
            }
        }
    }

    public boolean matches(String ip) {
        IpRange targetIpRange = new IpRange(ip);
        long targetIpLong = targetIpRange.getFrom();

        int start = 0;
        int end = sortedIpList.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            IpRange ipRange = sortedIpList.get(mid);
            if (targetIpLong < ipRange.getFrom()) {
                end = mid - 1;
            } else if (targetIpLong > ipRange.getTo()) {
                start = mid + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public Collection<String> getRequiredIps() {
        return this.requiredIps;
    }

}
