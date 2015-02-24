class AgreementInstallment < ActiveRecord::Base

  validates_presence_of :value, :status, :dueDate
end
