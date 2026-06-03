clc;
close all;



if exist('out', 'var')
    G_vec_sim = out.get('G_vec_sim');
    S_aloha_sim = out.get('S_aloha_sim');
    Pcol_aloha_sim = out.get('Pcol_aloha_sim');
    S_csma_sim = out.get('S_csma_sim');
    Pcol_csma_sim = out.get('Pcol_csma_sim');
    S_aloha_theory_sim = out.get('S_aloha_theory_sim');
    S_slotted_theory_sim = out.get('S_slotted_theory_sim');
end


G = getLastVector(G_vec_sim);
S_A = getLastVector(S_aloha_sim);
P_A = getLastVector(Pcol_aloha_sim);
S_C = getLastVector(S_csma_sim);
P_C = getLastVector(Pcol_csma_sim);
S_A_theory = getLastVector(S_aloha_theory_sim);
S_slot_theory = getLastVector(S_slotted_theory_sim);

% График пропускной способности
figure;
plot(G, S_A, 'LineWidth', 1.5);
hold on;
plot(G, S_C, 'LineWidth', 1.5);
plot(G, S_A_theory, '--', 'LineWidth', 1.5);
plot(G, S_slot_theory, ':', 'LineWidth', 1.5);
grid on;

xlabel('Нагрузка сети G');
ylabel('Пропускная способность S');
title('Результаты Simulink: пропускная способность');
legend('Pure ALOHA', 'CSMA', 'Pure ALOHA теория', 'Slotted ALOHA теория', ...
       'Location', 'best');

saveas(gcf, 'simulink_graph_throughput.png');

% График вероятности коллизии
figure;
plot(G, P_A, 'LineWidth', 1.5);
hold on;
plot(G, P_C, 'LineWidth', 1.5);
grid on;

xlabel('Нагрузка сети G');
ylabel('Вероятность коллизии');
title('Результаты Simulink: вероятность коллизии');
legend('Pure ALOHA', 'CSMA', 'Location', 'best');

saveas(gcf, 'simulink_graph_collision_probability.png');

% Таблица результатов
resultsSimulink = table(G', S_A', P_A', S_C', P_C', ...
    'VariableNames', {'G', 'S_ALOHA', 'Pcol_ALOHA', 'S_CSMA', 'Pcol_CSMA'});

disp(resultsSimulink);

writetable(resultsSimulink, 'simulink_results_aloha_csma.xlsx');


function v = getLastVector(x)

    if isa(x, 'timeseries')
        x = x.Data;
    end

    x = squeeze(x);

    if isvector(x)
        v = x(:).';
    else

        if size(x, 2) >= size(x, 1)
            v = x(end, :);
        else
            v = x(:, end).';
        end
    end
end