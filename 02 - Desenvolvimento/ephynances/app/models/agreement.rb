class Agreement < ActiveRecord::Base

  validates_presence_of :totalPrice, :contractorAgreementNumber, :physisAgreementNumber, :expiryDate, :status
  validates_uniqueness_of :contractorAgreementNumber, :physisAgreementNumber
end
