%BalancePlayer
x = [72, 12, 325, 165, 172, 261, 620, 141, 915]; 

%BonusSquarePlayer
%y = [9, 1000, 954, 979, 975, 960, 915, 988, 866];

plot(z,x);
hold on
%plot(z,y);

h_legend=legend('BalanceOnRackPlayer');
set(h_legend,'FontSize',14);
%legend('BalanceOnRackPlayer');
xlabel('Vowels', 'FontSize',14);
ylabel('Wins', 'FontSize',14);

%BalancePlayer
x = [73, 1, 507, 212, 262, 368, 838, 134, 1206]; 

%HighScoreWordPlayer
%y = [9, 1000, 954, 979, 975, 960, 915, 988, 866];

plot(z,x, 'g');
%hold on
%plot(z,y);
hold off
h_legend=legend('vs BonusSquarePlayer', 'vs HighScoreWordPlayer');
set(h_legend,'FontSize',14);
%legend('BalanceOnRackPlayer');
%title('BalanceOnRackPlayer wins', 'FontSize', 16);
xlabel('Vowels', 'FontSize',14);
ylabel('Wins', 'FontSize',14);
