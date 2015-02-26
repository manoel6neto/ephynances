# encoding: UTF-8
class AgreementResponsiblesController < ApplicationController
  before_action :set_agreement_responsible, only: [:show, :edit, :update, :destroy]

  # GET /agreement_responsibles
  # GET /agreement_responsibles.json
  def index
    @agreement_responsibles = AgreementResponsible.all
  end

  # GET /agreement_responsibles/1
  # GET /agreement_responsibles/1.json
  def show
  end

  # GET /agreement_responsibles/new
  def new
    @agreement_responsible = AgreementResponsible.new
  end

  # GET /agreement_responsibles/1/edit
  def edit
  end

  # POST /agreement_responsibles
  # POST /agreement_responsibles.json
  def create
    @agreement_responsible = AgreementResponsible.new(agreement_responsible_params)

    respond_to do |format|
      if @agreement_responsible.save
        format.html { redirect_to @agreement_responsible, notice: 'Agreement responsible was successfully created.' }
        format.json { render :show, status: :created, location: @agreement_responsible }
      else
        format.html { render :new }
        format.json { render json: @agreement_responsible.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /agreement_responsibles/1
  # PATCH/PUT /agreement_responsibles/1.json
  def update
    respond_to do |format|
      if @agreement_responsible.update(agreement_responsible_params)
        format.html { redirect_to @agreement_responsible, notice: 'Agreement responsible was successfully updated.' }
        format.json { render :show, status: :ok, location: @agreement_responsible }
      else
        format.html { render :edit }
        format.json { render json: @agreement_responsible.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /agreement_responsibles/1
  # DELETE /agreement_responsibles/1.json
  def destroy
    @agreement_responsible.destroy
    respond_to do |format|
      format.html { redirect_to agreement_responsibles_url, notice: 'Agreement responsible was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_agreement_responsible
      @agreement_responsible = AgreementResponsible.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def agreement_responsible_params
      params.require(:agreement_responsible).permit(:name, :email, :phone, :cellPhone, :cpf)
    end
end
