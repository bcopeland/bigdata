% simple pagerank in matlab, for easy testing
function [R] = pagerank(A, R, d, epsilon);

N = size(A)(1);
E = ones(N);
R_last = R;
delta = epsilon;
while (delta >= epsilon)
    R = (d * A  + (1-d)/N * E) * R_last;
    dist = R - R_last;
    delta = sqrt(dot(dist,dist));
    R_last = R;
end

% function [R] = pagerank(A, E, d, epsilon);
%
% Here's the version given in the paper, yields same
% result.
%
% R_last = E / sum(E);
% A = d * A
% delta = epsilon
% while (delta >= epsilon)
%     R = A * R_last;
%     d = sum(R_last) - sum(R);
%     R += d * E;
%     dist = R - R_last;
%     delta = sqrt(dot(dist,dist));
%     R_last = R;
% end
