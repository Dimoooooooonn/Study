clear;
clc;
close all;


% Параметры моделирования
T_packet = 1;
T_sim = 3000;
nPoints = 20;
G_vec = linspace(0.1, 4, nPoints);

% Параметры CSMA
tau = 0.01 * T_packet;   
backoffMax = 1 * T_packet; 

S_aloha = zeros(size(G_vec));
Pcol_aloha = zeros(size(G_vec));

S_csma = zeros(size(G_vec));
Pcol_csma = zeros(size(G_vec));

% Основной цикл по нагрузке
for i = 1:length(G_vec)
    G = G_vec(i);

    [S_aloha(i), Pcol_aloha(i)] = simulatePureAloha(G, T_sim, T_packet);
    [S_csma(i), Pcol_csma(i)] = simulateCSMA(G, T_sim, T_packet, tau, backoffMax);
end

S_aloha_theory = G_vec .* exp(-2 * G_vec);
S_slotted_theory = G_vec .* exp(-G_vec);

% График пропускной способности
figure;
plot(G_vec, S_aloha, 'LineWidth', 1.5);
hold on;
plot(G_vec, S_csma, 'LineWidth', 1.5);
plot(G_vec, S_aloha_theory, '--', 'LineWidth', 1.5);
plot(G_vec, S_slotted_theory, ':', 'LineWidth', 1.5);
grid on;

xlabel('Нагрузка сети G');
ylabel('Пропускная способность S');
title('Зависимость пропускной способности от нагрузки сети');
legend('Pure ALOHA, моделирование', ...
       'CSMA, моделирование', ...
       'Pure ALOHA, теория', ...
       'Slotted ALOHA, теория', ...
       'Location', 'best');

% График вероятности коллизий
figure;
plot(G_vec, Pcol_aloha, 'LineWidth', 1.5);
hold on;
plot(G_vec, Pcol_csma, 'LineWidth', 1.5);
grid on;

xlabel('Нагрузка сети G');
ylabel('Вероятность коллизии');
title('Зависимость вероятности коллизии от нагрузки сети');
legend('Pure ALOHA', 'CSMA', 'Location', 'best');

resultsTable = table(G_vec', S_aloha', Pcol_aloha', S_csma', Pcol_csma', ...
    'VariableNames', {'G', 'S_ALOHA', 'Pcol_ALOHA', 'S_CSMA', 'Pcol_CSMA'});

disp(resultsTable);

save('results_aloha_csma.mat', 'G_vec', 'S_aloha', 'Pcol_aloha', ...
     'S_csma', 'Pcol_csma', 'S_aloha_theory', 'S_slotted_theory');

writetable(resultsTable, 'results_aloha_csma.xlsx');

save('results_aloha_csma.mat', 'G_vec', 'S_aloha', 'Pcol_aloha', ...
     'S_csma', 'Pcol_csma', 'S_aloha_theory', 'S_slotted_theory', ...
     'resultsTable');

function [S, P_collision] = simulatePureAloha(G, T_sim, T_packet)
    arrivalTimes = generatePoissonArrivals(G, T_sim);

    totalPackets = length(arrivalTimes);

    if totalPackets == 0
        S = 0;
        P_collision = 0;
        return;
    end

    collided = false(totalPackets, 1);

    for k = 1:totalPackets
        currentStart = arrivalTimes(k);
        currentEnd = currentStart + T_packet;

        for j = 1:totalPackets
            if j ~= k
                otherStart = arrivalTimes(j);
                otherEnd = otherStart + T_packet;

                if currentStart < otherEnd && otherStart < currentEnd
                    collided(k) = true;
                    break;
                end
            end
        end
    end

    successfulPackets = sum(~collided);
    collidedPackets = sum(collided);

    S = successfulPackets * T_packet / T_sim;

    P_collision = collidedPackets / totalPackets;
end


function [S, P_collision] = simulateCSMA(G, T_sim, T_packet, tau, backoffMax)

    eventTimes = generatePoissonArrivals(G, T_sim);

    successfulPackets = 0;
    collidedPackets = 0;
    totalStartedTransmissions = 0;

    channelBusyUntil = 0;

    while ~isempty(eventTimes)

        [t, index] = min(eventTimes);
        eventTimes(index) = [];

        if t > T_sim
            break;
        end

        if t < channelBusyUntil
            newAttemptTime = channelBusyUntil + rand() * backoffMax;

            if newAttemptTime <= T_sim
                eventTimes(end + 1) = newAttemptTime;
            end

            continue;
        end

        collisionIndexes = find(eventTimes <= t + tau);

        transmissionStarts = [t; eventTimes(collisionIndexes)];
        eventTimes(collisionIndexes) = [];

        numberOfTransmitters = length(transmissionStarts);
        totalStartedTransmissions = totalStartedTransmissions + numberOfTransmitters;

        channelBusyUntil = max(transmissionStarts) + T_packet;

        if numberOfTransmitters == 1
            successfulPackets = successfulPackets + 1;
        else
            collidedPackets = collidedPackets + numberOfTransmitters;
        end
    end

    S = successfulPackets * T_packet / T_sim;

    if totalStartedTransmissions == 0
        P_collision = 0;
    else
        P_collision = collidedPackets / totalStartedTransmissions;
    end
end


function arrivalTimes = generatePoissonArrivals(lambda, T_sim)
    arrivalTimes = [];
    currentTime = 0;

    while currentTime < T_sim
        interArrivalTime = -log(rand()) / lambda;
        currentTime = currentTime + interArrivalTime;

        if currentTime < T_sim
            arrivalTimes(end + 1, 1) = currentTime;
        end
    end

    arrivalTimes = sort(arrivalTimes);
end

