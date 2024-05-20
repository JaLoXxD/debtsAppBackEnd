DELETE FROM debt_priority;

INSERT INTO debt_priority (ID, NAME, DESCRIPTION, GLOBAL, COLOR) VALUES (1, 'LOW', 'debt.priority.low', true, '#23ed26');
INSERT INTO debt_priority (ID, NAME, DESCRIPTION, GLOBAL, COLOR) VALUES (2, 'MEDIUM', 'debt.priority.medium', true, '#dfe527');
INSERT INTO debt_priority (ID, NAME, DESCRIPTION, GLOBAL, COLOR) VALUES (3, 'HIGH', 'debt.priority.high', true, '#e82929');