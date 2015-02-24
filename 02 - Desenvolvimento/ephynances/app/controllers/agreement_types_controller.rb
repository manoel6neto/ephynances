class AgreementTypesController < ApplicationController
  before_action :set_agreement_type, only: [:show, :edit, :update, :destroy]

  # GET /agreement_types
  # GET /agreement_types.json
  def index
    @agreement_types = AgreementType.all
  end

  # GET /agreement_types/1
  # GET /agreement_types/1.json
  def show
  end

  # GET /agreement_types/new
  def new
    @agreement_type = AgreementType.new
  end

  # GET /agreement_types/1/edit
  def edit
  end

  # POST /agreement_types
  # POST /agreement_types.json
  def create
    @agreement_type = AgreementType.new(agreement_type_params)

    respond_to do |format|
      if @agreement_type.save
        format.html { redirect_to @agreement_type, notice: 'Agreement type was successfully created.' }
        format.json { render :show, status: :created, location: @agreement_type }
      else
        format.html { render :new }
        format.json { render json: @agreement_type.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /agreement_types/1
  # PATCH/PUT /agreement_types/1.json
  def update
    respond_to do |format|
      if @agreement_type.update(agreement_type_params)
        format.html { redirect_to @agreement_type, notice: 'Agreement type was successfully updated.' }
        format.json { render :show, status: :ok, location: @agreement_type }
      else
        format.html { render :edit }
        format.json { render json: @agreement_type.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /agreement_types/1
  # DELETE /agreement_types/1.json
  def destroy
    @agreement_type.destroy
    respond_to do |format|
      format.html { redirect_to agreement_types_url, notice: 'Agreement type was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_agreement_type
      @agreement_type = AgreementType.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def agreement_type_params
      params.require(:agreement_type).permit(:type)
    end
end
