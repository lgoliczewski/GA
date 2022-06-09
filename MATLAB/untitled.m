T1 = readtable('C:\Users\mikol\Desktop\GA\Test1\MutationTest\MutationTestry48pinsert.csv');
T2 = readtable('C:\Users\mikol\Desktop\GA\Test1\MutationTest\MutationTestry48pinvert.csv');
T3 = readtable('C:\Users\mikol\Desktop\GA\Test1\MutationTest\MutationTestry48pRSM.csv');

K1 = table2array(T1);
K2 = table2array(T2);
K3 = table2array(T3);

x1 = K1(:,1);
y1 = K1(:,2);
x2 = K2(:,1);
y2 = K2(:,2);
x3 = K3(:,1);
y3 = K3(:,2);

P = plot(x1,y1,x2,y2,x3,y3);
xlabel('Czas [ms]') 
ylabel('Wartość najlepszego aktualnego rozwiązania') 
[TT,TT2] = title('Zależność najlepszego rozwiązania od czasu - ry48p.atsp','FontSize',16)
legend({'invert','insert','RSM'});
set(P,"Marker",".");
grid on;
xlim([0,60000])